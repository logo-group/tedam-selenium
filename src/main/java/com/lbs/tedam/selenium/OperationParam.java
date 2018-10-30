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

import org.openqa.selenium.WebDriver;

import com.lbs.tedam.util.EnumsV2.SeleniumByType;
import com.lbs.tedam.util.EnumsV2.TestStepType;

public class OperationParam {

	private WebDriver driver;
	private long waitingTime;
	private SeleniumByType type;
	private String tag;
	private String value;
	private Integer testStepId;
	private TestStepType testStepType;

	public OperationParam(WebDriver driver, long waitingTime, SeleniumByType type, String tag, String value,
			Integer testStepId, TestStepType testStepType) {
		this.driver = driver;
		this.waitingTime = waitingTime;
		this.type = type;
		this.tag = tag;
		this.value = value;
		this.testStepId = testStepId;
		this.testStepType = testStepType;
	}

	public SeleniumByType getType() {
		return type;
	}

	public void setType(SeleniumByType type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public long getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(long waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Integer getTestStepId() {
		return testStepId;
	}

	public void setTestStepId(Integer testStepId) {
		this.testStepId = testStepId;
	}

	public TestStepType getTestStepType() {
		return testStepType;
	}

	public void setTestStepType(TestStepType testStepType) {
		this.testStepType = testStepType;
	}

}
