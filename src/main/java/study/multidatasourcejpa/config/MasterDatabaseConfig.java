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
        // master repository가 존재하는 패키지
        // ##주의## slave repository와 패키지가 동일하면 안됨
        basePackages = "study.multidatasourcejpa.repository.master",
        // EntityManager 빈 이름
        entityManagerFactoryRef = "masterEntityManager",
        // transactionManager 빈 이름
        transactionManagerRef = "masterTransactionManager"
)
public class MasterDatabaseConfig {
    private final Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean masterEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        // master datasource 설정
        em.setDataSource(masterDataSource());

        // 도메인 경로 설정 (도메인은 Master와 Slave 둘다 같아도 됨)
        String[] domainPath = new String[]{"study.multidatasourcejpa.domain"};
        em.setPackagesToScan(domainPath);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Jpa 환경 설정
        HashMap<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        propertyMap.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        propertyMap.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
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
