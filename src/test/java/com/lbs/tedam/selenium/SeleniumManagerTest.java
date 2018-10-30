/*
* Copyright 2014-2019 Logo Business Solutions
* (a.k.a. LOGO YAZILIM SAN. VE TIC. A.S)
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*/

package com.lbs.tedam.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lbs.tedam.bsh.utils.ScriptService;

public class SeleniumManagerTest {

	private ScriptService scriptService = null;
	private SeleniumUtil seleniumUtil = null;
	private SeleniumManager manager = null;

	@Before
	public void beforeTest() {
		scriptService = new ScriptService(true);
		seleniumUtil = new SeleniumUtil(scriptService);
		manager = new SeleniumManager(seleniumUtil, scriptService);
	}

	@After
	public void afterTest() {
		scriptService = null;
		seleniumUtil = null;
		manager = null;
	}

	@Test
	public void testClickOperation() {
		String[] args = "browser!equals!CHROME url!equals!http://tedam.logo.com.tr:8080/TEDAMFaceV2/login.html timeRecording!equals!true version!equals!1 testSetId!equals!1 testCaseId!equals!1 ButtonClick!equals!attribute!ips!id!ips!click!ips!button-submit!ps!0!ts!1"
				.split(" ");
		manager.executeOperations(args);
	}

	@Test
	public void testClickOperationWithInnerText() {
		String[] args = "browser!equals!CHROME url!equals!http://testdivalogin.logo.com.tr/Login.aspx timeRecording!equals!true version!equals!1 testSetId!equals!6006 testCaseId!equals!27504 FormFill!equals!24158!ts!166098!fn!1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!btnlogin!ps!0!ts!166099!fn!1%0 ButtonClick!equals!innertext!ips!click!ips!Satış!ps!0!ts!166100!fn!MasterFrame:0%0 ButtonClick!equals!innertext!ips!click!ips!Pos!spc!Satış!spc!Faturası!spc!-!spc!Yeni!ps!0!ts!166101!fn!MasterFrame:0%0 FormFill!equals!24160!ts!166108!fn!MasterFrame_161024:1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!FiyatGuncellemeSifresiBtn!ps!0!ts!166109!fn!MasterFrame_161024:1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!SatisKaydetBtn!ps!0!ts!166110!fn!MasterFrame_161024:1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!tablist1-tab2!ps!0!ts!166111!fn!MasterFrame_161024:1%0 FormFill!equals!24161!ts!166112!fn!MasterFrame_161024:1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!btnListele!ps!0!ts!166113!fn!MasterFrame_161024:1%0 ComponentDoubleClick!equals!xpath!ips!read!ips!//*[@id='Table1']/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]/span[1]!ts!166114!fn!MasterFrame_161024:1%0 Verify!equals!24162!ps!0!ts!166115!fn!MasterFrame_161024:1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!IptalBtn!ps!0!ts!166116!fn!MasterFrame_161024:1%0"
				.split(" ");
//		String[] args = "browser!equals!CHROME url!equals!http://testdivalogin.logo.com.tr/Login.aspx timeRecording!equals!true version!equals!1 testSetId!equals!6005 testCaseId!equals!27437 FormFill!equals!23963!ts!165527 ButtonClick!equals!attribute!ips!id!ips!click!ips!btnlogin!ps!0!ts!165528 ButtonClick!equals!innertext!ips!click!ips!Satış!ps!0!ts!165530!fn!MasterFrame:0%0 ButtonClick!equals!innertext!ips!click!ips!Pos!spc!Satış!spc!Faturası!spc!-!spc!Yeni!ps!0!ts!165531!fn!MasterFrame:0%0 FormFill!equals!24006!ts!165532 ButtonClick!equals!attribute!ips!id!ips!click!ips!FiyatGuncellemeSifresiBtn!ps!0!ts!165533 ButtonClick!equals!attribute!ips!id!ips!click!ips!SatisKaydetBtn!ps!0!ts!165534 ButtonClick!equals!attribute!ips!id!ips!click!ips!tablist1-tab2!ps!0!ts!165537 FormFill!equals!24007!ts!165536!fn!MasterFrame_161024:1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!btnListele!ps!0!ts!165538 Verify!equals!24008!ps!0!ts!165539"
//				.split(" ");
		manager.executeOperations(args);
	}
	
	@Test 
	public void testClickOperationWithInnerTextWithIE() {
		String[] args = "browser!equals!IE url!equals!http://testdivalogin.logo.com.tr/Login.aspx timeRecording!equals!true version!equals!1 testSetId!equals!6055 testCaseId!equals!27562 FormFill!equals!24365!ts!166665!fn!1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!btnlogin!ps!0!ts!166666!fn!1%0 ButtonClick!equals!innertext!ips!click!ips!Satış!ps!0!ts!166667!fn!MasterFrame:0%0 ButtonClick!equals!innertext!ips!click!ips!Pos!spc!Satış!spc!Faturası!spc!-!spc!Yeni!ps!0!ts!166668!fn!MasterFrame:0%0 FormFill!equals!24378!ts!166669!fn!MasterFrame_161024:1%0"
				.split(" ");
		manager.executeOperations(args);
	}
	

	@Test
	public void testDoubleClickOperation() {
		String[] args = "browser!equals!CHROME url!equals!http://testdivalogin.logo.com.tr/Login.aspx timeRecording!equals!true version!equals!1 testSetId!equals!6006 testCaseId!equals!27437 ComponentDoubleClick!equals!xpath!ips!write!ips!//*[@id='s2id_Plasiyer_PlasiyerKodu']/a[1]/span[1]!ts!166095!fn!MasterFrame_161024:1%0"
				.split(" ");
//		String[] args = "browser!equals!CHROME url!equals!http://testdivalogin.logo.com.tr/Login.aspx timeRecording!equals!true version!equals!1 testSetId!equals!6005 testCaseId!equals!27437 FormFill!equals!23963!ts!165527 ButtonClick!equals!attribute!ips!id!ips!click!ips!btnlogin!ps!0!ts!165528 ButtonClick!equals!innertext!ips!click!ips!Satış!ps!0!ts!165530!fn!MasterFrame:0%0 ButtonClick!equals!innertext!ips!click!ips!Pos!spc!Satış!spc!Faturası!spc!-!spc!Yeni!ps!0!ts!165531!fn!MasterFrame:0%0 FormFill!equals!24006!ts!165532 ButtonClick!equals!attribute!ips!id!ips!click!ips!FiyatGuncellemeSifresiBtn!ps!0!ts!165533 ButtonClick!equals!attribute!ips!id!ips!click!ips!SatisKaydetBtn!ps!0!ts!165534 ButtonClick!equals!attribute!ips!id!ips!click!ips!tablist1-tab2!ps!0!ts!165537 FormFill!equals!24007!ts!165536!fn!MasterFrame_161024:1%0 ButtonClick!equals!attribute!ips!id!ips!click!ips!btnListele!ps!0!ts!165538 Verify!equals!24008!ps!0!ts!165539"
//				.split(" ");
		manager.executeOperations(args);
	}

	@Test
	public void testClickOperationWithElementNotFound() {
		String[] args = "browser!equals!CHROME url!equals!http://tedam.logo.com.tr:8080/TEDAMFaceV2/login.html timeRecording!equals!true version!equals!1 testSetId!equals!1 testCaseId!equals!1 ButtonClick!equals!attribute!ips!id!ips!click!ips!button-submit-not-found!ps!0!ts!1"
				.split(" ");
		manager.executeOperations(args);
	}

	@Test
	public void testClickOperationByXPath() {
		String[] args = "browser!equals!CHROME url!equals!http://tedam.logo.com.tr:8080/TEDAMFaceV2/login.html timeRecording!equals!true version!equals!1 testSetId!equals!1 testCaseId!equals!1 ButtonClick!equals!xpath!ips!click!ips!//*[@id=\"button-submit\"]!ps!0!ts!1"
				.split(" ");
		manager.executeOperations(args);
	}

	@Test
	public void testClickOperationWithFirefox() {
		String[] args = "browser!equals!FIREFOX url!equals!http://tedam.logo.com.tr:8080/TEDAMFaceV2/login.html timeRecording!equals!true version!equals!1 testSetId!equals!1 testCaseId!equals!1 ButtonClick!equals!attribute!ips!id!ips!click!ips!button-submit!ps!0!ts!1"
				.split(" ");
		manager.executeOperations(args);
	}

	@Test
	public void testWriteOperation() {
		String[] args = "browser!equals!CHROME url!equals!http://tedam.logo.com.tr:8080/TEDAMFaceV2/login.html timeRecording!equals!true version!equals!1 testSetId!equals!1 testCaseId!equals!1 FormFill!equals!24126!ts!1"
				.split(" ");
		manager.executeOperations(args);
	}

	@Test
	public void testReadOperation() {
		String[] args = "browser!equals!CHROME url!equals!http://tedam.logo.com.tr:8080/TEDAMFaceV2/login.html timeRecording!equals!true version!equals!1 testSetId!equals!1 testCaseId!equals!1 Verify!equals!24127!ps!0!ts!2"
				.split(" ");
		manager.executeOperations(args);
	}

	@Test
	public void testAllOperation() {
		String[] args = "browser!equals!CHROME url!equals!http://tedam.logo.com.tr:8080/TEDAMFaceV2/login.html timeRecording!equals!true version!equals!1 testSetId!equals!1 testCaseId!equals!1 FormFill!equals!24126!ts!1 Verify!equals!24127!ps!0!ts!2 ButtonClick!equals!attribute!ips!id!ips!click!ips!button-submit!ps!0!ts!1"
				.split(" ");
		manager.executeOperations(args);
	}

}
