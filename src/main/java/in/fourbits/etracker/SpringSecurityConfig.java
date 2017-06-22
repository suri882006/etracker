package in.fourbits.etracker;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import in.fourbits.etracker.dao.EtrackerUserCrudRepository;
import in.fourbits.etracker.entity.EtrackerUser;

/**
 * Class to enable spring security for etracker application
 * @author Suraj Acharya
 * 
 * @EnableWebSecurity This Annotation is responsible for Authentication and
 *                    authorization for web requests
 * 
 * @EnableGlobalMethodSecurity(prePostEnabled = true) TODO: Check the usage
 *                                            ofthis
 *
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = Logger.getLogger(SpringSecurityConfig.class);

	@Autowired
	private EtrackerUserCrudRepository userRepository;

	@Autowired
	private DataSource datasource;

	/**
	 * Responsible to identify and define which resource access should be
	 * authenticated and type of authentication to be used
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable();
		// .authenticationEntryPoint(authEntryPoint);
	}

	/**
	 * Providing implementation for interface {@link UserDetailsService} to
	 * search and load user by useName and throw
	 * {@link UsernameNotFoundException} exception if userName does not exist in
	 * store
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				logger.info("Trying loadbyusername for username --> " + username);
				EtrackerUser etrackerUser = userRepository.findByUserName(username);
				if (etrackerUser != null) {
					return new User(etrackerUser.getUserName(), etrackerUser.getPassword(), true, true, true, true,
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("could not find the user '" + username + "'");
				}
			}

		};
	}

	/**
	 * {@link SecurityBuilder} used to create an {@link AuthenticationManager}.
	 * Allows for easily building in memory authentication, LDAP authentication,
	 * JDBC based authentication, adding {@link UserDetailsService}, and adding
	 * {@link AuthenticationProvider}'s.
	 * 
	 * @param auth
	 *            AuthenticationManagerBuilder
	 * @throws Exception
	 * 
	 *             Usages :-
	 * 
	 *             InMemoryAuthentication -
	 *             auth.inMemoryAuthentication().withUser("suraj").password("dummy"
	 *             ).roles("USER");
	 * 
	 *             Authentication using userDetailService -
	 *             auth.userDetailsService(userDetailsService());
	 * 
	 *             JDBC based authentication -
	 *             auth.jdbcAuthentication().dataSource(datasource).usersByUsernameQuery
	 *             ("select username, password, true from tbluserlogin where
	 *             username=?" ).authoritiesByUsernameQuery("select
	 *             username,'USER' from tbluserlogin where username=?");
	 * 
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
	}
}
