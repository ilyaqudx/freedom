



1-读流程
2-写流程
3-keepalive
4-线程模型
	Acceptor
	Connector 在完成连接后finishConnect后应该pause Connector线程
5-限流
6-过滤器链
	考虑过滤器的位置,在CODEC后或是其他?为了避免使用者在编解码前使用了线程池,造成数据问题,考虑在CODEC后.但如果涉及到加解密则必须在编码前
	发送过程的编码应该由发送线程处理
7-异常处理
8-检测IO idle