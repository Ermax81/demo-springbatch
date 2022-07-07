package com.example.batchprocessing.demo;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing //adds many critical beans that support jobs and save you a lot of leg work
                       //provides a memory-based database
@AllArgsConstructor
public class BatchConfiguration {

    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;

    // Creates an ItemReader.
    // It looks for a file called sample-data.csv and parses each line item with enough information to turn it into a Person.
    @Bean
    public FlatFileItemReader<Person> reader() {
      return new FlatFileItemReaderBuilder<Person>()
              .name("personItemReader")
              .resource(new ClassPathResource("sample-data.csv"))
              .lineTokenizer(new DelimitedLineTokenizer() {{
                  setNames("firstName", "lastName", "age");
                  setDelimiter(";");
              }
              })
              //.delimited().names(new String[]{"firstName", "lastName"}) //comma delimiter: ','
              .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                  setTargetType(Person.class);
              }})
              .build();
    }

    // Creates an instance of the PersonItemProcessor that you defined earlier,
    // meant to convert the data to upper case.
    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor(springValidatorItem());
    }

//    @Bean
//    public BeanValidatingItemProcessor<Person> itemValidator() throws Exception {
//        BeanValidatingItemProcessor<Person> validator = new BeanValidatingItemProcessor<>();
//        validator.setFilter(true);
//        validator.afterPropertiesSet();
//
//        return validator;
//    }

    // Creates an ItemWriter.
    // This one is aimed at a JDBC destination
    // and automatically gets a copy of the dataSource created by @EnableBatchProcessing.
    // It includes the SQL statement needed to insert a single Person, driven by Java bean properties.
    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return  new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name, age) VALUES (:firstName, :lastName, :age)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {

        // JOB_NAME in table BATCH_JOB_INSTANCE i
        // JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS... in table BATCH_JOB_EXECUTION e
        // i.JOB_INSTANCE_ID = e.JOB_INSTANCE_ID
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer()) //need an incrementer, because jobs use a database to maintain execution state
                .listener(listener)

                .start(step1)

                .next(step2()).on("NOOP").end()

                .from(step2()).on("COMPLETED").to(stepThink()).next(stepDone())

                .from(step2()).on("FAILED").to(stepFail()).end()

                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) throws Exception {

        // STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS in table BATCH_STEP_EXECUTION
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader())
                .processor(processor())
                //.processor(itemValidator())
                .writer(writer)
                .build();
    }

    @Bean
    public Tasklet executeTasklet() {
        String message = "2-Executing new step (step2)";
        return new MessageTasklet(message, false);
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .allowStartIfComplete(true)
                .tasklet(executeTasklet())
                .build();
    }

    @Bean
    public Tasklet failTasklet() {
        String message = "Failing to build something...";
        return new MessageTasklet(message, true);
    }

    @Bean
    public Step stepFail() {
        return stepBuilderFactory.get("stepFail")
                .allowStartIfComplete(true)
                .tasklet(failTasklet())
                .build();
    }

    @Bean
    public Tasklet doneTasklet() {
        String message = "I'm done";
        return new MessageTasklet(message, true);
    }

    @Bean
    public Step stepDone() {
        return stepBuilderFactory.get("stepDone")
                .allowStartIfComplete(true)
                .tasklet(doneTasklet())
                .build();
    }

    @Bean
    public Tasklet thinkTasklet() {
        String message = "<Thinking of ...>";
        return new MessageTasklet(message, true);
    }

    @Bean
    public Step stepThink() {
        return stepBuilderFactory.get("stepThink")
                .allowStartIfComplete(true)
                .tasklet(thinkTasklet())
                .build();
    }

    // VALIDATOR
    @Bean
    public org.springframework.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public <T> SpringValidator<T> springValidatorItem() {
        SpringValidator<T> springValidator = new SpringValidator<>();
        springValidator.setValidator(localValidatorFactoryBean());
        return springValidator;
    }

}
