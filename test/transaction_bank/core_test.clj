(ns transaction-bank.core-test
  (:require [midje.sweet :refer :all]
    	      [ring.mock.request :as mock]
            [cheshire.core :refer :all]
            [transaction-bank.core :refer :all]))

(facts "Returns instructions on root route"
  (let [response	(application	(mock/request	:get	"/"))]
    (fact "Answer status is 200"
      (:status	response)	=>	200)
    (fact "Body text is an instruction"
      (:body	response)	=>	"*** Access the README to execute tests ***")))

(facts "Invalid route does not exist"
  (let [response	(application	(mock/request	:get	"/not-found"))]
    (fact "The error code is 404"
      (:status	response)	=>	404)
    (fact "The body text is 'Not Found!'"
      (:body response) => "Not Found!")))

(facts "Send data to service"
  (let [response (application (-> (mock/request :post "/transactions")
                                  (mock/json-body {:cardIsActive "true"
                                                   :limit 6100
                                                   :denylist ["Padaria" "Farmacia" "Mercado"]
                                                   :merchant "Mercado"
                                                   :amount 300
                                                   :timestamp "2019-07-29 20:49:55"})))]
    (fact "The code is 200"
      (:status	response)	=>	200)
    (fact ""
      (:body response) => "[]")))
