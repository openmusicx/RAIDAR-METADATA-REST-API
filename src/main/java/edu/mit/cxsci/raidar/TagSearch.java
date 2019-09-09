//package edu.mit.cxsci.raidar;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import edu.mit.cxsci.raidar.asset.document.RaidarAsset;
//import edu.mit.cxsci.raidar.asset.repository.RaidarAssetRepository;
//
//public class TagSearch {
//	@Autowired
//	private static RaidarAssetRepository assetRepository;
//
//	public static void main(String[] args) {
//		List<RaidarAsset> assets = assetRepository.findByTags("pop");
//		System.out.print(assets);
//	}
//
//}
