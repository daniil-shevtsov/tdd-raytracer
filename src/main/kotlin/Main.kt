import file.createPmm
import ray.practice.litSphereOnCanvas


fun main(args: Array<String>) {
    createPmm(name = "lit-sphere.pmm", content = litSphereOnCanvas())
}