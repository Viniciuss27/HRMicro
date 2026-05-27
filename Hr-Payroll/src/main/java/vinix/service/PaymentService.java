package vinix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vinix.entities.Payment;
import vinix.entities.Worker;
import vinix.feignclients.WorkerFeignClient;

@Service
public class PaymentService {

	@Autowired
	private WorkerFeignClient feign;
	
	public Payment getPayment(Long workerId, Integer days) {

	    var response = feign.findById(workerId);
	    Worker worker = response.getBody();

	    return new Payment(worker.getName(), worker.getDailyIncome(), days);
	}

}
