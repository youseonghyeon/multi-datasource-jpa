package study.multidatasourcejpa.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = "study.multidatasourcejpa.repository.master",
        entityManagerFactoryRef = "masterEntityManager",
        transactionManagerRef = "masterTransactionManager"
)
public class MasterDatabaseConfig {
    private final Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean masterEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(masterDataSource());

        // 도메인 경로
        String[] domainPath = {"study.multidatasourcejpa.domain"};
        em.setPackagesToScan(domainPath);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        log.info(propertyMap.get("hibernate.hbm2ddl.auto").toString());
        propertyMap.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(propertyMap);

        return em;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager masterTransactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(masterEntityManager().getObject());

        return tm;
    }


}
