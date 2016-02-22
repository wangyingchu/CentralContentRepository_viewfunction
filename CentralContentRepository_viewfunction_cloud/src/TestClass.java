import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.testng.Assert;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.testNGTest.TestCaseDataConstant;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.ContentQueryHelper;
import com.viewfunction.contentRepository.util.helper.PropertyQueryHelper;
import com.viewfunction.contentRepository.util.helper.TextContent;

public class TestClass {
	
	public static void main(String[] args) throws RepositoryException{
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			
			RootContentObject xx=cs.getRootContentObject("oak:index");

			List<BaseContentObject> xxxListxx=xx.getSubContentObjects(null);
			
			
			for(BaseContentObject contentObject:xxxListxx){
				System.out.println(contentObject.getContentObjectName());
				
			}
			
			
			
		//	RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);
		//	cs.addRootContentObject(rco);			
			RootContentObject addedRco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);			
		//	Assert.assertNotNull(addedRco);	
		//	BaseContentObject binaryCo=addedRco.addSubContentObject("contentQueryFolder", null, true);
		//	Assert.assertNotNull(binaryCo);	
			
			BaseContentObject folderCo=addedRco.getSubContentObject("contentQueryFolder");
			Assert.assertNotNull(folderCo);	
		
			
			
			
			
			
			
			
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();	
			//Assert.assertNotNull(coh);	
			/*
			File f1=new File("testBinaryContentFile/01300000029584120462690206338.jpg");
			boolean b=coh.addBinaryContent(folderCo,f1, "01300000029584120462690206338.jpg_DECS",true);
			Assert.assertTrue(b);					
			boolean b3=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/3380232995318967763.jpg"), "3380232995318967763.jpg",true);
			Assert.assertTrue(b3);		
			boolean b5=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/6_roger.jpg"), "6_roger.jpg",true);
			Assert.assertTrue(b5);
			File f2=new File("testBinaryContentFile/Beyonce - Halo10M.mp3");
			boolean b7=coh.addBinaryContent(folderCo,f2, "Beyonce - Halo10M.mp3_DECS",true);
			Assert.assertTrue(b7);			
			boolean b9=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CommonBase Event_SituationData_V20.doc"), "CommonBase Event_SituationData_V20.doc",true);
			Assert.assertTrue(b9);			
			boolean b11=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Communications_Management_wp_FINAL.pdf"), "Communications_Management_wp_FINAL.pdf",true);
			Assert.assertTrue(b11);			
			boolean b13=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CSS Speech Bubbles.zip"), "CSS Speech Bubbles.zip",true);
			Assert.assertTrue(b13);			
			boolean b15=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/IMG_1716.JPG"), "IMG_1716.JPG",true);
			Assert.assertTrue(b15);		
			boolean b17=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/o_0c0a24112bd5.jpg"), "o_0c0a24112bd5.jpg",true);
			Assert.assertTrue(b17);			
			boolean b19=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/ScalaTutorial.pdf"), "ScalaTutorial.pdf",true);
			Assert.assertTrue(b19);			
			boolean b21=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Typophone_4_by_Angelman8.jpg"), "Typophone_4_by_Angelman8.jpg",true);
			Assert.assertTrue(b21);		
			boolean b23=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/vaadin621.pdf"), "vaadin621.pdf",true);
			Assert.assertTrue(b23);					
			List<BinaryContent> bcol=coh.getBinaryContents(folderCo);			
			Assert.assertEquals(12, bcol.size(),"Added binaryContents number should be 12");				
			
			File f11=new File("testTextContentFile/accents.txt");
			boolean b1a=coh.addTextContent(folderCo,f11, "accents.txt_DECS",true);
			Assert.assertTrue(b1a);				
			boolean b3a=coh.addTextContent(folderCo,new File("testTextContentFile/AINSI.txt"), "AINSI.txt",true);
			Assert.assertTrue(b3a);			
			boolean b5a=coh.addTextContent(folderCo,new File("testTextContentFile/BigEndian.txt"), "BigEndian.txt",true);
			Assert.assertTrue(b5a);			
			boolean b7a=coh.addTextContent(folderCo,new File("testTextContentFile/ChineseContent.txt"), "ChineseContent.txt",true);
			Assert.assertTrue(b7a);			
			boolean b9a=coh.addTextContent(folderCo,new File("testTextContentFile/Unicode.txt"), "Unicode.txt",true);
			Assert.assertTrue(b9a);			
			boolean b11a=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8.txt"), "UTF8.txt",true);
			Assert.assertTrue(b11a);			
			boolean b13a=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8BOM.txt"), "UTF8BOM.txt",true);
			Assert.assertTrue(b13a);			
			boolean b15a=coh.addTextContent(folderCo,new File("testTextContentFile/UTF-8-test.txt"), "UTF-8-test.txt",true);
			Assert.assertTrue(b15a);			
			List<TextContent> bcola=coh.getTextContents(folderCo);			
			Assert.assertEquals(8, bcola.size(),"Added binaryContents number should be 8");	
			*/
			
			
			
			
			
			
			BinaryContent bc=	coh.getBinaryContent(folderCo, "Communications_Management_wp_FINAL.pdf");
			
			
			
			System.out.println(bc);
			ContentQueryHelper cqh=ContentComponentFactory.getContentQueryHelper();
			List<BinaryContent> bl0=cqh.selectBinaryContentsByTitle(folderCo, "aadin");
			System.out.println(bl0.get(0).getContentName());
			
			List<BinaryContent> bl1=cqh.selectBinaryContentsByMimeType(folderCo, "pdf");
			System.out.println(bl1.get(0).getMimeType());
		
			//List<BinaryContent> bl2=cqh.selectBinaryContentsByFullTextSearch(folderCo, "Executive summary");
			List<BinaryContent> bl2=cqh.selectBinaryContentsByFullTextSearch(folderCo, "ommonBase");
			System.out.println(bl2.size());
			
		//	System.out.println(bl2.get(0).getMimeType());
			
	//		BaseContentObject folderPath=cs.getContentObjectByAbsPath("/ActivitySpace_ContentStore/Space_ContentStore");
		
		
	//	ContentQueryHelper cqh=ContentComponentFactory.getContentQueryHelper();
		
		
		//List<BinaryContent> bl0=cqh.selectBinaryContentsByTitle(folderPath, "aadin");
	//	List<BinaryContent> bl0=cqh.selectBinaryContentsByFullTextSearch(folderPath, "a");
		
		//List<BinaryContent> bl0=cqh.selectBinaryContentsByMimeType(folderPath, "jpeg");
		//List<TextContent> bl0=cqh.selectTextContentsByTitle(folderPath, "dinplu");
		
		//List<TextContent> bl0=cqh.selectTextContentsByEncoding(folderPath, "");
		
		
		/*
		System.out.println(bl0.size());
		
		for(BinaryContent b:bl0){
			
			System.out.println(b.getContentName());
			
			
			
		}
		*/
		
		
		
		/*
		ContentOperationHelper coh = ContentComponentFactory.getContentOperationHelper();
		List<BinaryContent> bl1=coh.getBinaryContents(folderPath);
		
		System.out.println(bl1.size());
		for(BinaryContent b:bl1){
			
			System.out.println(b.getContentName());
			System.out.println(b.getMimeType());
			
			
			
		}
		*/
		
		
		
		cs.closeContentSpace();
		
		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			
		}	
		
		
		
	}

}
