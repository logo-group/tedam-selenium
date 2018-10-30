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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import com.lbs.tedam.bsh.utils.ScriptService;
import com.lbs.tedam.model.TestReport;
import com.lbs.tedam.recorder.TestStepTimeRecord;
import com.lbs.tedam.recorder.TestStepTimeRecorder;
import com.lbs.tedam.recorder.TimeRecorder;
import com.lbs.tedam.util.Constants;
import com.lbs.tedam.util.Enums.ScriptParameterSahi;
import com.lbs.tedam.util.Enums.StatusMessages;
import com.lbs.tedam.util.EnumsV2.BrowserType;
import com.lbs.tedam.util.HasLogger;
import com.lbs.tedam.util.PropUtils;

public class SeleniumManager implements HasLogger {

	private WebDriver driver;
	private SeleniumUtil seleniumUtil;
	private ScriptService scriptService;

	public SeleniumManager(SeleniumUtil seleniumUtil, ScriptService scriptService) {
		this.seleniumUtil = seleniumUtil;
		this.scriptService = scriptService;
	}

	private WebDriver createWebDriver(BrowserType browserType) {
		switch (browserType) {
		case CHROME:
			return new ChromeDriver(new ChromeOptions());
		case FIREFOX:
			return new FirefoxDriver(new FirefoxOptions());
		case IE:
			InternetExplorerOptions options = new InternetExplorerOptions();
			options.ignoreZoomSettings();
			return new InternetExplorerDriver(options);
		default:
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void executeOperations(String[] parameterArray) {
		Map<String, Object> parameterMap = seleniumUtil.splitParameters(parameterArray);
		Map<ScriptParameterSahi, String> scriptParametersMap = (Map<ScriptParameterSahi, String>) parameterMap
				.get(Constants.WEB_SCRIPT_PARAMETERS_MAP);
		List<TestReport> reportList = new ArrayList<TestReport>();

		TimeRecorder<TestStepTimeRecord> recorder = createTimeRecorder(scriptParametersMap);
		setExtraConfig();
		prepareDriver(scriptParametersMap);
		List<Operation> operationList = seleniumUtil.createOperationList(parameterMap);
		handleExecution(recorder, reportList, operationList);
		quit();
		finalizeExecuteOperations(scriptParametersMap, recorder, reportList);
	}

	private TimeRecorder<TestStepTimeRecord> createTimeRecorder(Map<ScriptParameterSahi, String> scriptParametersMap) {
		Boolean timeRecording = Boolean.valueOf(scriptParametersMap.get(ScriptParameterSahi.TIME_RECORDING));

		TimeRecorder<TestStepTimeRecord> recorder = new TestStepTimeRecorder();
		recorder.setRecording(timeRecording);
		return recorder;
	}

	private void handleExecution(TimeRecorder<TestStepTimeRecord> recorder, List<TestReport> reportList,
			List<Operation> operationList) {
		Map<String, Long> waitingTimeMap = findWaitingTime();
		for (Operation operation : operationList) {
			TestReport report = new TestReport(operation.getOperationParam().getTestStepType().getValue(),
					Constants.EMPTY_STRING);
			try {
				operation.getOperationParam().setDriver(getDriver());
				operation.getOperationParam().setWaitingTime(waitingTimeMap.get(operation.getClass().getName()));
				recorder.record(new Runnable() {

					@Override
					public void run() {
						logOperation(operation);
						operation.executeOperation();
						finalizeTestReport(operation, report);
					}
				});
			} catch (UnhandledAlertException e) {
				Alert alert = operation.getOperationParam().getDriver().switchTo().alert();
				alert.accept();
				finalizeTestReport(operation, report);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}
			} catch (Exception e) {
				handleException(report, e);
				break;
			} finally {
				handleFinally(recorder.getRecordList(), reportList, operation, report);
			}
		}
	}

	private void finalizeTestReport(Operation operation, TestReport report) {
		report.setStatusMsg(StatusMessages.SUCCEEDED.getStatus());
		report.setMessage("Operation completed with tag: " + operation.getOperationParam().getTag() + " by "
				+ operation.getOperationParam().getType());
	}

	private void logOperation(Operation operation) {
		OperationParam operationParam = operation.getOperationParam();
		getLogger().info("Executing operation: TestStepId: " + operationParam.getTestStepId() + ", TestStepType: "
				+ operationParam.getTestStepType().getValue() + ", By type: " + operationParam.getType() + ", Tag: "
				+ operationParam.getTag() + ", Value: " + operationParam.getValue());
	}

	private void handleFinally(List<TestStepTimeRecord> timeRecordList, List<TestReport> reportList,
			Operation operation, TestReport report) {
		reportList.add(report);
		setTestStepInfos(timeRecordList, reportList, operation);
	}

	private void handleException(TestReport report, Exception e) {
		getLogger().error(e.getMessage());
		report.setMessage(e.getMessage());
		report.setStatusMsg(StatusMessages.FAILED.getStatus());
	}

	private void prepareDriver(Map<ScriptParameterSahi, String> scriptParametersMap) {
		BrowserType browserType = BrowserType.valueOf(scriptParametersMap.get(ScriptParameterSahi.BROWSER));
		String url = scriptParametersMap.get(ScriptParameterSahi.URL);
		setDriver(createWebDriver(browserType));
		getDriver().manage().window().maximize();
		getDriver().get(url);
	}

	private void setTestStepInfos(List<TestStepTimeRecord> timeRecordList, List<TestReport> reportList,
			Operation operation) {
		scriptService.setTestStepIdOfLastRecord(timeRecordList, operation.getOperationParam().getTestStepId());
		scriptService.setTestStepIdToReportList(reportList, operation.getOperationParam().getTestStepId());
	}

	private void finalizeExecuteOperations(Map<ScriptParameterSahi, String> scriptParametersMap,
			TimeRecorder<TestStepTimeRecord> recorder, List<TestReport> reportList) {
		String release = scriptParametersMap.get(ScriptParameterSahi.VERSION);
		String testSetId = scriptParametersMap.get(ScriptParameterSahi.TEST_SET_ID);
		String testCaseId = scriptParametersMap.get(ScriptParameterSahi.TEST_CASE_ID);
		String resultFileName = Constants.REPORT_HEADER_RUN + testSetId + Constants.REPORT_TEST_CASE + testCaseId
				+ Constants.FILE_EXTENSION_XLS;
		String tempFilePath = PropUtils.getProperty(Constants.TEMP_FILE_PATH);
		scriptService.printTestReport(reportList, resultFileName, tempFilePath);
		scriptService.fillReleaseInfo(recorder.getRecordList(), release);
		scriptService.saveTestStepTimeRecordList(recorder.getRecordList());
	}

	private Map<String, Long> findWaitingTime() {
		Map<String, Long> waitingTimeMap = new HashMap<>();

		long formFillWaitingTime = Long.valueOf(PropUtils.getProperty("waitingTime.formFill"));
		long buttonClickWaitingTime = Long.valueOf(PropUtils.getProperty("waitingTime.buttonClick"));
		long verifyWaitingTime = Long.valueOf(PropUtils.getProperty("waitingTime.verify"));
		long doubleClickWaitingTime = Long.valueOf(PropUtils.getProperty("waitingTime.doubleClick"));
		long waitWaitingTime = Long.valueOf(PropUtils.getProperty("waitingTime.wait"));

		waitingTimeMap.put(WriteOperation.class.getName(), formFillWaitingTime);
		waitingTimeMap.put(ClickOperation.class.getName(), buttonClickWaitingTime);
		waitingTimeMap.put(ReadOperation.class.getName(), verifyWaitingTime);
		waitingTimeMap.put(DoubleClickOperation.class.getName(), doubleClickWaitingTime);
		waitingTimeMap.put(WaitOperation.class.getName(), waitWaitingTime);

		return waitingTimeMap;
	}

	private void setExtraConfig() {
		String chromeDriverPath = PropUtils.getProperty("driver.path.chrome");
		String firefoxDriverPath = PropUtils.getProperty("driver.path.firefox");
		String ieDriverPath = PropUtils.getProperty("driver.path.ie");
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
		System.setProperty("webdriver.ie.driver", ieDriverPath);
	}

	private void quit() {
		getDriver().quit();
	}

	private WebDriver getDriver() {
		return driver;
	}

	private void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public static void main(String[] args) {
		ScriptService scriptService = new ScriptService();
		SeleniumUtil seleniumUtil = new SeleniumUtil(scriptService);
		SeleniumManager manager = new SeleniumManager(seleniumUtil, scriptService);
		manager.executeOperations(args);
	}

}
