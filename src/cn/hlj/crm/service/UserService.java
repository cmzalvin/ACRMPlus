package cn.hlj.crm.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hlj.crm.dao.UserMapper;
import cn.hlj.crm.entity.User;
import cn.hlj.crm.orm.Page;
import cn.hlj.crm.orm.PropertyFilter;
import cn.hlj.crm.util.ParamsUtils;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Transactional(readOnly = true)
	public User login(String name, String password) {
		User user = userMapper.getByName(name);

		if (user != null && user.getPassword().equals(password) && user.getEnabled() == 1) {
			return user;
		}

		return null;
	}

	@Transactional(readOnly = true)
	public Page<User> getPage(Page<User> page) {
		long totalElements = userMapper.getTotalElements();
		page.setTotalElements(totalElements);

		int fromIndex = (page.getPageNo() - 1) * page.getPageSize() + 1;
		int endIndex = fromIndex + page.getPageSize();

		List<User> content = userMapper.getContent(fromIndex, endIndex);
		page.setContent(content);

		return page;
	}

	@Transactional
	public void save(User user) {
		userMapper.save(user);
	}

	@Transactional
	public void delete(Long id) {
		userMapper.delete(id);
	}

	@Transactional(readOnly = true)
	public User get(Long id) {
		return userMapper.get(id);
	}

	@Transactional
	public void update(User user) {
		userMapper.update(user);
	}

	@Transactional(readOnly = true)
	public Page<User> getPage(Page<User> page, Map<String, Object> params) {
		// 1. 把 params 转为 PropertyFilter 的集合
		List<PropertyFilter> filters = PropertyFilter.parseParamsToPropertyFilters(params);

		// 2. 再把 PropertyFilter 的集合转为 mybatis 可以用的 Map.
		Map<String, Object> mybatisParams = ParamsUtils.parsePropertyFiltersToMybatisParams(filters);

		long totalElements = userMapper.getTotalElementsWithConditions(mybatisParams);
		page.setTotalElements(totalElements);

		int fromIndex = (page.getPageNo() - 1) * page.getPageSize() + 1;
		int endIndex = fromIndex + page.getPageSize();
		mybatisParams.put("firstIndex", fromIndex);
		mybatisParams.put("endIndex", endIndex);

		List<User> content = userMapper.getContentWithConditions(mybatisParams);
		page.setContent(content);

		return page;
	}

	@Transactional(readOnly = true)
	public List<User> getAll() {
		return userMapper.getAll();
	}

	@Transactional(readOnly = true)
	public User getByName(String name) {
		return userMapper.getByName(name);
	}
}
