package edu.mit.cxsci.raidar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import edu.mit.cxsci.raidar.asset.document.RaidarAsset;
import edu.mit.cxsci.raidar.asset.repository.RaidarAssetRepository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class TagSearchTest {
	@Autowired
	private RaidarAssetRepository assetRepository;
	
	
	 @Before
	    public void setUp() throws Exception {
	 }
	 
	 @Test
	    public void testAllAssets(){
		 List<RaidarAsset> assets = assetRepository.findAll();
		 System.out.println(assets);
	    }
	 @Test
	    public void testSearchByTag(){
		 String tags = "pop,mizan";
		 List<RaidarAsset> assets = assetRepository.findByTagsIn(Arrays.asList(tags.split("\\s*,\\s*")));
		 System.out.println(assets);
	    }
	 
	 @After
	    public void tearDown() throws Exception {
	      //this.assetTagRepository.deleteAll();
	    }

}
