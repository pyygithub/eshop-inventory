# eshop-inventory
电商库存服务中实现缓存+数据库双写一致性保障方案

更新数据的时候，根据数据的唯一标识，将操作路由之后，发送到一个jvm内部的队列中 

读取数据的时候，如果发现数据不在缓存中，那么将重新读取数据+更新缓存的操作，根据数据唯一标识路由之后，也发送同一个jvm内部的队列中 

一个队列对应一个工作线程 

每个工作线程串行拿到对应的操作，然后一条一条的执行 

这样的话，一个数据变更的操作，先执行，删除缓存，然后再去更新数据库，但是还没完成更新 

此时如果一个读请求过来，读到了空的缓存，那么可以先将缓存更新的请求发送到队列中，此时会在队列中积压，然后同步等待缓存更新完成 

这里有一个优化点，一个队列中，其实多个更新缓存请求串在一起是没意义的，因此可以做过滤，如果发现队列中已经有一个更新缓存的请求了，那么就不用再放个更新请求操作进去了，直接等待前面的更新操作请求完成即可 

待那个队列对应的工作线程完成了上一个操作的数据库的修改之后，才会去执行下一个操作，也就是缓存更新的操作，此时会从数据库中读取最新的值，然后写入缓存中 

如果请求还在等待时间范围内，不断轮询发现可以取到值了，那么就直接返回; 如果请求等待的时间超过一定时长，那么这一次直接从数据库中读取当前的旧值 

int h; 
return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16); 

(queueNum - 1) & hash 

1 、线程池+内存队列初始化 

@Bean 
public ServletListenerRegistrationBean servletListenerRegistrationBean(){ 
    ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean(); 
    servletListenerRegistrationBean.setListener(new InitListener()); 
    return servletListenerRegistrationBean; 
} 

java web 应用，做系统的初始化，一般在哪里做呢？ 

ServletContextListener 里面做，listener，会跟着整个web应用的启动，就初始化，类似于线程池初始化的构建 

spring boot 应用，Application，搞一个listener的注册 

2 、两种请求对象封装 

3 、请求异步执行Service封装 

4 、两种请求Controller接口封装 

5 、读请求去重优化 

6 、空数据读请求过滤优化 

队列 

对一个商品的库存的数据库更新操作已经在内存队列中了 

然后对这个商品的库存的读取操作，要求读取数据库的库存数据，然后更新到缓存中，多个读 

这多个读，其实只要有一个读请求操作压到队列里就可以了 

其他的读操作，全部都wait那个读请求的操作，刷新缓存，就可以读到缓存中的最新数据了 

如果读请求发现redis缓存中没有数据，就会发送读请求给库存服务，但是此时缓存中为空，可能是因为写请求先删除了缓存，也可能是数据库里压根儿没这条数据 

如果是数据库中压根儿没这条数据的场景，那么就不应该将读请求操作给压入队列中，而是直接返回空就可以了 

都是为了减少内存队列中的请求积压，内存队列中积压的请求越多，就可能导致每个读请求hang住的时间越长，也可能导致多个读请求被hang住 


