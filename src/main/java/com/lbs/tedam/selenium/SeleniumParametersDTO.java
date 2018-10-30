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

import com.lbs.tedam.util.EnumsV2.TestStepType;

public class SeleniumParametersDTO {

	private TestStepType testStepType;
	private String stepTypeParameter;
	private int testStepId = 0;

	public SeleniumParametersDTO(TestStepType testStepType, String stepTypeParameter, int testStepId) {
		this.testStepType = testStepType;
		this.stepTypeParameter = stepTypeParameter;
		this.testStepId = testStepId;
	}

	public TestStepType getTestStepType() {
		return testStepType;
	}

	public void setTestStepType(TestStepType testStepType) {
		this.testStepType = testStepType;
	}

	public String getStepTypeParameter() {
		return stepTypeParameter;
	}

	public void setStepTypeParameter(String stepTypeParameter) {
		this.stepTypeParameter = stepTypeParameter;
	}

	public int getTestStepId() {
		return testStepId;
	}

	public void setTestStepId(int testStepId) {
		this.testStepId = testStepId;
	}

}