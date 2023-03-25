package study.multidatasourcejpa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = "study.multidatasourcejpa.repository.slave",
        entityManagerFactoryRef = "slaveEntityManager",
        transactionManagerRef = "slaveTransactionManager"
)
public class SlaveDatabaseConfig {
    private final Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean slaveEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(slaveDatasource());
        // 도메인 경로
        String[] domainPath = {"study.multidatasourcejpa.domain"};
        em.setPackagesToScan(domainPath);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        propertyMap.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(propertyMap);
        return em;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager slaveTransactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(slaveEntityManager().getObject());
        return tm;
    }
}
