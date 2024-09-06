package repositories;

import entities.Consumption;

import java.util.List;
import java.util.UUID;

public interface ConsumptionRepository {

    List<Consumption> findByUserId(UUID userId);
    void save(Consumption consommation);
    double findTotalConsumptionByUser(UUID userId);

}
