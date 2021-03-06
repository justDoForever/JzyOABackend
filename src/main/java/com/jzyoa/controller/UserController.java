package com.jzyoa.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jzyoa.entity.PageResult;
import com.jzyoa.entity.Result;
import com.jzyoa.pojo.TbUser;
import com.jzyoa.service.UserService;
import com.jzyoa.util.DateUtil;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/checkLogin")
	public Result checkLogin(@RequestBody TbUser user) {
		return userService.checkLogin(user);
	}

	@RequestMapping("/checkLoginNameExist")
	public boolean checkLoginNameExist(String loginName) {
		return userService.checkLoginNameExist(loginName);
	}

	@RequestMapping("/list")
	public PageResult list(@RequestBody TbUser user, Integer pageNum, Integer pageSize) {
		PageResult pageResult = userService.list(user, pageNum, pageSize);
//		List<TbHospital> hospitalList = pageResult.getRows();
//		for (TbHospital hospital : hospitalList) {
//			hospital.setDepartmentList(departmentController.listByHosId(hospital.getHosId()));
//		}
		return pageResult;
	}

	@RequestMapping("/save")
	public Result save(@RequestBody TbUser addUser) {
		addUser.setRegisterTime(DateUtil.getMintueDate());
		addUser.setUserStatus(1);
		return userService.save(addUser);
	}

	@RequestMapping("/update")
	public Result update(@RequestBody TbUser addUser) {
		return userService.update(addUser);
	}
	
	
	@RequestMapping("/delete")
	public Result delete(Integer userId) {
		return userService.delete(userId);
	}
	
	@RequestMapping("/cancelDelete")
	public Result cancelDelete(Integer userId) {
		return userService.cancelDelete(userId);
	}
	
	@RequestMapping("/getByUserId")
	public TbUser getByUserId(Integer userId) {
		return userService.getByUserId(userId);
	}
}
