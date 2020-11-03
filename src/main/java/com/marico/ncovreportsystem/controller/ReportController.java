package com.marico.ncovreportsystem.controller;

import com.marico.ncovreportsystem.entity.PageResult;
import com.marico.ncovreportsystem.entity.Result;
import com.marico.ncovreportsystem.pojo.TbDepartment;
import com.marico.ncovreportsystem.pojo.TbHospital;
import com.marico.ncovreportsystem.pojo.TbReport;
import com.marico.ncovreportsystem.pojo.TbSpecimen;
import com.marico.ncovreportsystem.service.DepartmentService;
import com.marico.ncovreportsystem.service.DictionaryService;
import com.marico.ncovreportsystem.service.DoctorService;
import com.marico.ncovreportsystem.service.HospitalService;
import com.marico.ncovreportsystem.service.ManagerService;
import com.marico.ncovreportsystem.service.ReportService;
import com.marico.ncovreportsystem.service.SpecimenService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportService reportService;
	@Autowired
	private SpecimenController specimenController;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private DepartmentService departService;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private ManagerService magagerService;

	@Autowired
	private SpecimenService specimenService;

	@RequestMapping("/save")
	public Result save(@RequestBody TbReport addReport, HttpServletRequest request, HttpServletResponse response) {
		return reportService.save(getDetails(addReport), request, response);
	}

	@RequestMapping("/update")
	public Result update(@RequestBody TbReport updateReport, HttpServletRequest request, HttpServletResponse response) {
		return reportService.update(getDetails(updateReport), request, response);
	}

	@RequestMapping("/batchSaveAsNegative")
	public Result batchSaveAsNegative(@RequestBody TbSpecimen searchSpecimen, String createTime,int reporterId, int reviewerId,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		createTime= URLDecoder.decode(createTime,"utf-8");
		List<TbSpecimen> specimenList = specimenService.listAll(searchSpecimen);
		// 查找对应的医院名称
		TbHospital hospital = hospitalService.getById(searchSpecimen.getHosId());
		// 查找对应的科室名称
		TbDepartment department = departService.getById(searchSpecimen.getDeptId());
		return reportService.batchSaveAsNegative(request, response, specimenList, reporterId, reviewerId,
				hospital.getHosName(), department==null?null:department.getDeptName(),createTime);
	}

	public TbReport getDetails(TbReport report) {
		if (report == null)
			return null;
		report.setReferenceValue(dictionaryService.getById(report.getReferenceValueId()));
		report.setReporter(magagerService.getById(report.getReporterId()));
		report.setReviewer(magagerService.getById(report.getReviewerId()));
		report.setSpecimen(specimenController.getDetailsById(report.getSpecimenId()));
		report.setSpecimenResult(dictionaryService.getById(report.getSpecimenResultId()));
		return report;
	}

	@RequestMapping("/getDetailsBySpecimenId")
	public TbReport getDetailsBySpecimenId(Integer specimenId) {
		return getDetails(reportService.getBySpecimenId(specimenId));
	}

	@RequestMapping("/getById")
	public TbReport getById(Integer reportId) {
		return reportService.getById(reportId);
	}

	@RequestMapping("/getDetailsById")
	public TbReport getDetailsById(Integer reportId) {
		return getDetails(reportService.getById(reportId));
	}

	public PageResult listRoughly(TbReport searchReport, int pageNum, Integer pageSize) {
		return reportService.listRoughly(searchReport, pageNum, pageSize);
	}

	public PageResult listByCreateTimeSpan(String startTime, String endTime, Integer pageNum, int pageSize) {
		PageResult listByCreateTimeSpan = reportService.listByCreateTimeSpan(startTime, endTime, pageNum, pageSize);
		List<TbReport> reportList = listByCreateTimeSpan.getRows();
		for (TbReport report : reportList) {
			report.setReferenceValue(dictionaryService.getById(report.getReferenceValueId()));
			report.setReporter(magagerService.getById(report.getReporterId()));
			report.setReviewer(magagerService.getById(report.getReviewerId()));
			report.setSpecimenResult(dictionaryService.getById(report.getSpecimenResultId()));
		}
		return listByCreateTimeSpan;
	}

	public List<TbReport> listAll(TbReport searchReport) {
		return reportService.listAll(searchReport);
	}

}
