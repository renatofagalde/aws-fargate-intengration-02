package br.com.likwi.awsfargate.repository;

import br.com.likwi.awsfargate.model.ProductEventKey;
import br.com.likwi.awsfargate.model.ProductEventLog;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ProductEventRepository extends CrudRepository<ProductEventLog, ProductEventKey> {

    List<ProductEventLog> findAllByPk(String code);

    List<ProductEventLog> findAllByPkAndSkStartingWith(String code, String eventType);

}
