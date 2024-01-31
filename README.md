Project includes tests cases in CarsTests.java that checks creation of car object with POST method (https://retoolapi.dev/KCuEUu/cars):
- createCarResponseIsSuccessWithoutId
- createCarResponseIsSuccessWithStringPrice
- createCarResponseIsSuccessWithOnlyPrice
- createCarResponseIsSuccessWithOnlyBrand
- createCarResponseIsSuccessWithId
  * success code (201) and response body to include populated data
- createCarWitDuplicatedIdFails
  * error code (500) and response body to include error message when attempt to create car with already existing id
 
