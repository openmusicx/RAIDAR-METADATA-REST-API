/**

* The MIT License (MIT)

* Copyright (c) 2019 RAIDAR

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
**/

package edu.mit.cxsci.raidar.api;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.google.gson.JsonObject;

import edu.mit.cxsci.raidar.asset.document.RaidarAsset;
import edu.mit.cxsci.raidar.asset.repository.RaidarAssetRepository;
import edu.mit.cxsci.raidar.util.GsonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mizanul H. Chowdhuury, MIT
 * @version 1.0
 **/

@RestController
@RequestMapping("/v1/api")
@CrossOrigin("*")
public class AssetAPI {

	@Autowired
	private RaidarAssetRepository assetRepository;

	private static Logger logger = LoggerFactory.getLogger(AssetAPI.class);
	
	@GetMapping(value = "/asset/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RaidarAsset>> getAssetSearchByTags(HttpServletRequest request, Model m) {
		List<RaidarAsset> assets = new ArrayList<RaidarAsset>();
		
		Enumeration enumeration = request.getParameterNames();
	    Map<String, Object> modelMap = new HashMap<>();
	    while(enumeration.hasMoreElements()){
	        String parameterName = (String) enumeration.nextElement();
	        List<String> values=Arrays.asList((request.getParameter(parameterName)).split(","));
	        System.out.println("TAGS:::values:::"+values);
	        if (parameterName.equalsIgnoreCase("mood")) {
	        	assets = assetRepository.findByMoodIn(values);
	        }
	        else if (parameterName.equalsIgnoreCase("genre")) {
	        	assets = assetRepository.findByGenreIn(values);
	        }
//	        else if (parameterName.equalsIgnoreCase("artist")) {
//	        	assets = assetRepository.findByArtistNameContaining(request.getParameter(parameterName));
//	        }
//	        else if (parameterName.equalsIgnoreCase("title")) {
//	        	assets = assetRepository.findByTitleContaining(request.getParameter(parameterName));
//	        }
	        else {
	        	System.out.println("TAGS:::parameterName:2::"+values);
	        	assets = assetRepository.findByTagsIn(values);
	        }
			
	    }
		
		return new ResponseEntity<List<RaidarAsset>>(assets, HttpStatus.OK);
	}

	@GetMapping(value = "/asset", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RaidarAsset>> getAssets(Model m) {
		List<RaidarAsset> assets = assetRepository.findAll();
		return new ResponseEntity<List<RaidarAsset>>(assets, HttpStatus.OK);
	}

	@GetMapping(value = "/asset/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RaidarAsset> getAsset(@PathVariable("id") String id, Model m) {
		Optional<RaidarAsset> asset = assetRepository.findById(id);
		if (asset != null) {
			return new ResponseEntity(asset, HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/asset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RaidarAsset> postAsset(@RequestBody RaidarAsset asset) {
		System.out.println(toJson(asset).toString());
		// logger.debug(asset.getAssetHeaderInfo().getLeader());
		asset = assetRepository.save(asset);
		return new ResponseEntity(asset, HttpStatus.OK);
	}

	@PutMapping(value = "/asset/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RaidarAsset> updateAsset(@PathVariable("id") String id, @RequestBody RaidarAsset asset) {
		// Optional<RaidarAsset> asset_old = assetRepository.findById(id);
		return assetRepository.findById(id).map(assetData -> {
			assetData.setMusicalWorkID(asset.getMusicalWorkID());
			assetData.setTitle(asset.getTitle());
			assetData.setArtistName(asset.getArtistName());
			assetData.setArtistId(asset.getArtistId());
			assetData.setSongWriterName(asset.getSongWriterName());
			assetData.setBpm(asset.getBpm());
			assetData.setMood(asset.getMood());
			assetData.setGenre(asset.getGenre());
			assetData.setType(asset.getType());
			assetData.setDuration(asset.getDuration());
			assetData.setKey(asset.getKey());
			assetData.setTags(asset.getTags());
			assetData.setCopyrightRegistrationNumber(asset.getCopyrightRegistrationNumber());
			assetData.setDateOfEntry(asset.getDateOfEntry());
			assetData.setIsrc(asset.getIsrc());
			RaidarAsset updatedAsset = assetRepository.save(assetData);
			return ResponseEntity.ok().body(updatedAsset);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/asset/{id}")
	public ResponseEntity<?> deleteAsset(@PathVariable("id") String id) {
		return assetRepository.findById(id).map(asset -> {
			assetRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	public JsonObject toJson(RaidarAsset asset) {
		return (JsonObject) GsonUtil.gson.toJsonTree(asset);
	}
}
