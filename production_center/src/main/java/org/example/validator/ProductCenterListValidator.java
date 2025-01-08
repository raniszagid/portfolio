package org.example.validator;

import org.example.entities.ProductionCenter;

import java.util.ArrayList;
import java.util.List;

public class ProductCenterListValidator {
    private final List<ProductionCenter> list;
    private final int maxProdCentersCount = 20;
    public ProductCenterListValidator(List<ProductionCenter> list) {
        this.list = list;
    }
    public void validate() {
        checkNotEmpty();
        String expectedDataSize = String.format("Ожидаемая размерность данных (числа положительные): " +
                "количество ПЦ – до %d", maxProdCentersCount);
        if (!checkDataSize())
            throw new ProductCenterDataException(expectedDataSize);
        if (!checkUnique())
            throw new ProductCenterDataException("Повторение id в списке производственных центров недопустимо");
        checkFirstAndLastCenter();
        checkCycleChains();
    }
    protected void checkNotEmpty() {
        if (list.isEmpty()) throw new ProductCenterDataException("Пустой список производственных центров");
    }
    protected void checkFirstAndLastCenter() {
        String message = "Должен быть ровно один %s производственный центр, в то время как в данном случае их %d";
        int ancestorsWithoutParents = (int) list.stream().filter(c -> c.getOutput() == 0).count();
        if (ancestorsWithoutParents != 1)
            throw new ProductCenterDataException(String.format(message, "начальный", ancestorsWithoutParents));
        int descendantsWithoutChildren = (int) list.stream().filter(c -> c.getInput() == 0).count();
        if (descendantsWithoutChildren != 1)
            throw new ProductCenterDataException(String.format(message, "конечный", descendantsWithoutChildren));
    }
    protected void checkCycleChains() {
        ProductionCenter father = list.stream().filter(c -> c.getOutput() == 0).findAny().orElseThrow();
        findChainsRecursive(father, new ArrayList<>());
    }
    private void findChainsRecursive(ProductionCenter currentNode, List<ProductionCenter> currentChain) {
        if (currentChain.contains(currentNode))
            throw new ProductCenterDataException("Циклические цепочки связей не допускаются");
        currentChain.add(currentNode);
        if (!currentNode.getChildren().isEmpty()) {
            for (ProductionCenter child : currentNode.getChildren()) {
                findChainsRecursive(child, new ArrayList<>(currentChain));
            }
        }
    }
    protected boolean checkDataSize() {
        int productionCenters = list.size();
        return productionCenters > 0 && productionCenters <= maxProdCentersCount;
    }
    protected boolean checkUnique() {
        return list.size() == list.stream().map(ProductionCenter::getId).distinct().count();
    }
}
