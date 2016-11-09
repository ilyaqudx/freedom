package freedom.bio.moudel.logon;

import freedom.bio.core.AbstractCommand;

public class LogonCommand extends AbstractCommand<LogonAccount, LogonSuccess>{

	@Override
	public LogonSuccess run(LogonAccount in, LogonSuccess out) throws Exception
	{
		System.out.println(in);
		return LogonUtils.buildLogonSuccess();
	}

	@Override
	public short getMainCmd() 
	{
		return 1;
	}

	@Override
	public short getSubCmd() 
	{
		return 100;
	}

}
