package tw.katy.com.dao;

import static org.junit.Assert.fail;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.katy.com.dao.impl.OrderDaoImpl;
import tw.katy.com.entity.BlessOrder;
import tw.katy.com.service.OrderService;
import tw.katy.com.util.ProxyUtils;

public class OrderDaoTest {

	private Logger log = LoggerFactory.getLogger(OrderDaoTest.class);
	
	OrderDao dao = (OrderDao) ProxyUtils.getProxyDao(new OrderDaoImpl());
	
	@Test
	public void testFindInPage() {
		
	}

	@Test
	public void testInster() {
		//OrderDaoImpl dao =  new OrderDaoImpl();
	/*	BlessOrder order1 = new BlessOrder("測式人員1", "09889922122", "彰化縣員林鎮仁愛路100號", null, LocalDateTime.now(), LocalDateTime.now());
		order1.setwCandlesFlag("Y");
		order1.setfCandlesFlag("N");*/
		/*try {
			int i =dao.insert(order1);
			log.debug("insert {}",order1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		OrderService service = new OrderService();
		try {
			/*order1 = service.insert(order1);
			log.debug("insert {}",order1);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdate() {
		/*BlessOrder order = dao.findById(1).get(0);
		order.setName("修改測式");
		int i =dao.update(order);
		log.debug("update {} 筆",i)*/;
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() {	
		/*List<BlessOrder> order = dao.findById(1);
		System.out.println(order);*/
	}


	@Test
	public void testFindAll() {
		List<BlessOrder> order = dao.findAll();
		System.out.println(order);
	}
	
	@Test
	public void testFindByCondition(){
		List<BlessOrder> order = dao.findByCondition(null,null,null, null);
		System.out.println(order);
	}
	
	

}
