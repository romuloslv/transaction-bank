(ns transaction-bank.card-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :refer :all]
            [transaction-bank.card :refer :all]))

(fact "Test current-acumulator"
  (current-acumulator 6100 0 1)  => 6100)

(fact "Test rules"
  (rules "true" 6100 ["Padaria" "Farmacia" "Mercado"]
         "Posto" 300 "2019-07-29 20:49:55")  => [])

(fact "Find how many merchants exceed the rules limit"
  (loop [x 0]
    (when (< x 10)
      (moment "Uber" (.format
                     (java.text.SimpleDateFormat.
                          "yyyy-MM-dd HH:mm:ss")
                          (new java.util.Date)))
      (recur (+ x 1))))
  (comparation)  => ["Uber"]
  (reset! all-transactions []))

(fact "Recalculated limit basead in error"
  (current-acumulator 5800 1456 5)  => 5800
  (current-acumulator 5800 1370 0)  => 4430)

(fact "Recalculated limit"
  (def amount 430)
  (rescue acumulator amount)  => 4000)

(fact "Test rules"
  (rules "false" 0 ["Padaria" "Farmacia" "Mercado"]
         "Farmacia" 20 "2019-07-29 20:49:55")  => [])

(fact "Test rules"
  (rules "false" 10 ["Padaria" "Farmacia" "Mercado"]
         "Farmacia" 20 "2019-07-29 20:49:55")  => [])

(fact "Test rules"
  (rules "false" 100 ["Padaria" "Farmacia" "Mercado"]
         "Farmacia" 20 "2019-07-29 20:49:55")  => [])

(fact "Test limit merchant"
  (loop [x 0]
    (when (< x 10)
      (rules "true" 100 ["Padaria" "Farmacia" "Mercado"]
             "Posto" 20 "2019-07-29 20:49:55")
      (recur (+ x 1))))
  (rules "true" 100 ["Padaria" "Farmacia" "Mercado"]
         "Posto" 20 "2019-07-29 20:49:55")  => [])

(fact "Record test of a transaction"
  (reset! all-transactions [])
  (def out {:merchant "Padaria"
            :timestamp "2019-07-29 18:13:52"
            :millisecond (now)})
  (moment "Padaria" "2019-07-29 18:13:52")	=>	[out]
  (reset! all-transactions []))

(facts "Tests defined by the execution rules"
  (fact "When the code is ten"
    (status-code 10)	=>	[{:error-body "it's all right."
                            :error-name "OK"}]
    (reset! erro ()))
  (fact "When the code is twenty"
    (status-code 20)	=>	[{:error-name "Unauthorized"
                            :error-body "Card Block in your APP."}]
    (reset! erro ()))
  (fact "When the code is thirty"
    (status-code 30)	=>	[{:error-name "Forbidden"
                            :error-body "Your request is authenticated but has insufficient limit."}]
    (reset! erro ()))
  (fact "When the code is forty"
    (status-code 40)	=>	[{:error-name "Method Not Allowed"
                            :error-body "You are using the card in a Blacklist establisment."}]
    (reset! erro ()))
  (fact "When the code is fifty"
    (status-code 50)	=>	[{:error-name "Buy Denied"
                            :error-body "Your buy is exceeding its limit."}]
    (reset! erro ()))
  (fact "When the code is sixty"
    (status-code 60)	=>	[{:error-name "First Buy"
                            :error-body "Your first purchase exceeds the 90% rate limit."}]
    (reset! erro ()))
  (fact "When the code is seventy"
    (status-code 70)	=>	[{:error-name "Merchant Limited"
                            :error-body "Merchant whith more of ten transactions."}]
    (reset! erro ()))
  (fact "When the code is eighty"
    (status-code 80)	=>	[{:error-name "Gateway Timeout"
                            :error-body "Many transactions in less than two minutes"}]
    (reset! erro ())))
