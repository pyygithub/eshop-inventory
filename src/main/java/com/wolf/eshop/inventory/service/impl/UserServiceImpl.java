package com.wolf.eshop.inventory.service.impl;



import com.alibaba.fastjson.JSONObject;
import com.wolf.eshop.inventory.mapper.UserMapper;
import com.wolf.eshop.inventory.model.User;
import com.wolf.eshop.inventory.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * 用户Service实现类
 * @author Administrator
 *
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public User findUserInfo() {
		return userMapper.findUserInfo();
	}

	@Override
	public User getCachedUserInfo() {
		stringRedisTemplate.opsForValue().set("cached_user_lisi", "{\"name\": \"lisi\", \"age\":28}");
		log.info("### redis set success. key={}", "cached_user_list");
		String userJSON = stringRedisTemplate.opsForValue().get("cached_user_lisi");
		JSONObject userJSONObject = JSONObject.parseObject(userJSON);
		
		User user = new User();
		user.setName(userJSONObject.getString("name"));   
		user.setAge(userJSONObject.getInteger("age"));  
		
		return user;
	}

}
