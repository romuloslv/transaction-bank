(ns transaction-bank.api
  (:require [transaction-bank.card :as card]))

(defn request-transaction
  "Forwards http transaction request to service."
  [request]
  (let [card-status (get request "cardIsActive")
        limit (.setScale (new BigDecimal (str (get request "limit"))) 2)
        denylist (get request "denylist")
        merchant (get request "merchant")
	      amount (.setScale (new BigDecimal (str (get request "amount"))) 2)
	      timestamp (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss")
                  (new java.util.Date))]
  (card/rules card-status limit denylist merchant amount timestamp)))
