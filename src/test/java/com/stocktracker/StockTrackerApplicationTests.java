package com.stocktracker;

import com.stocktracker.repositorylayer.entity.CustomerEntity;
import com.stocktracker.repositorylayer.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockTrackerApplicationTests {
	private Logger log = LoggerFactory.getLogger( StockTrackerApplicationTests.class );
	private CustomerRepository customerRepository;

	@Autowired
	public void setCustomerRepository( final CustomerRepository customerRepository )
	{
		log.info( "Dependency Injection CustomerRepository: " + customerRepository );
		this.customerRepository = customerRepository;
	}

	@Before
	public void load()
	{
		log.info( "Loading customer 1" );
		CustomerEntity customerEntity = new CustomerEntity();
		//customerEntity.setId( 1 );
		customerEntity.setEmail( "michael.earl.65@gmail.com" );
		customerEntity.setPassword( "usarmy84" );
		customerRepository.save( customerEntity );
	}

	@Override
	protected void finalize()
		throws Throwable
	{
	}

	@Test
	public void contextLoads()
	{
	}

	@Test
	public void testCustomer1()
	{
		log.info( "testCustomer1" );
		//assertTrue( customerRepository.exists( 1 ) );
	}

}
