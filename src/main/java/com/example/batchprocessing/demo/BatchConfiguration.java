package com.example.batchprocessing.demo;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
                  setNames("firstName", "lastName");
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
        return new PersonItemProcessor();
    }

    // Creates an ItemWriter.
    // This one is aimed at a JDBC destination
    // and automatically gets a copy of the dataSource created by @EnableBatchProcessing.
    // It includes the SQL statement needed to insert a single Person, driven by Java bean properties.
    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return  new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer()) //need an incrementer, because jobs use a database to maintain execution state
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

}
