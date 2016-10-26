package freedom.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import freedom.project.po.User;
import freedom.project.service.IUserService;
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private TransactionTemplate template;
	
	@Transactional
	@Override
	public void out(User from, User to, double amount)
	{
		template.execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(TransactionStatus arg0)
			{
				return null;
			}
		});
	}

}
