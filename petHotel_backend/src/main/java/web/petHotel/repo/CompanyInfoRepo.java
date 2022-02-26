package web.petHotel.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import web.petHotel.entities.CompanyInfo;

public interface CompanyInfoRepo extends ReactiveCrudRepository<CompanyInfo,String> {


}
