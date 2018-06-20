package au.com.redhat.labs.demos.todoapi.api.mongo;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = ToDoListMongoRepository.class)
public class MongoConfig extends AbstractReactiveMongoConfiguration {




    @Bean
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
//                .create(MongoClientSettings.builder()
//                        .serverSettings(ServerSettings.builder()
//                                .applyConnectionString(new ConnectionString("mongodb://localhost"))
//                                .build())
//                        .socketSettings(
//                                SocketSettings.builder()
//                                        .connectTimeout(1, TimeUnit.SECONDS)
//                                        .readTimeout(1, TimeUnit.SECONDS)
//                                        .build()
//                        )
//                        .build())
//                ;
    }

    @Override
    protected String getDatabaseName() {
        return "labs";
    }


    @Override
    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }


}