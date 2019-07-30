(ns transaction-bank.card
  (:require [clojure.java.io :as io]))

(def acumulator 0)

(def erro
  (atom ()))

(def all-transactions
  (atom []))

(defn now
  []
  (.getTime (java.util.Date.)))

(defn rescue
  "Change final value based on value received from current-accumulator"
  [args amount]
  (alter-var-root #'acumulator #(last [%1 (- args amount)])))

(defn moment
  "Saves the time at which a transaction was executed."
  [merchant timestamp]
  (swap! all-transactions conj {:merchant  merchant
                                :timestamp timestamp
                                :millisecond (now)}))

(defn comparation
  "Returns the amount of merchants with more than ten transactions"
  []
  (->> (mapv :merchant @all-transactions)
       sort
       (partition-by identity)
       (filter #(> (count %) 9))
       (mapv first)))

(defn current-acumulator
  "Calculates initial values ​​based on transaction-bank/resources/transactions.json file entry"
  [limit amount count-erro]
  (if (zero? count-erro)
    (if (> acumulator 0)
      (rescue acumulator amount)
      (rescue limit amount))
    (if (zero? acumulator)
      limit
      acumulator)))

(defn status-code
  [error-code]
  (cond
  (= error-code 10)
    (swap! erro conj {:error-name "OK"
                      :error-body "it's all right."})
  (= error-code 20)
    (swap! erro conj {:error-name "Unauthorized"
                      :error-body "Card Block in your APP."})
  (= error-code 30)
    (swap! erro conj {:error-name "Forbidden"
                      :error-body "Your request is authenticated but has insufficient limit."})
  (= error-code 40)
    (swap! erro conj {:error-name "Method Not Allowed"
                      :error-body "You are using the card in a Blacklist establisment."})
  (= error-code 50)
    (swap! erro conj {:error-name "Buy Denied"
                      :error-body "Your buy is exceeding its limit."})
  (= error-code 60)
    (swap! erro conj {:error-name "First Buy"
                      :error-body "Your first purchase exceeds the 90% rate limit."})
  (= error-code 70)
    (swap! erro conj {:error-name "Merchant Limited"
                      :error-body "Merchant whith more of ten transactions."})
  (= error-code 80)
    (swap! erro conj {:error-name "Gateway Timeout"
                      :error-body "Many transactions in less than two minutes"})
    :else (throw (Throwable. "An unknown error has occurred."))))

(defn output
  [limit merchant count-erro amount timestamp]
  (moment merchant timestamp)
  (if (zero? count-erro)
    (do
      (status-code 10)
      (def card-status true))
    (def card-status false))
  (with-open [wrtr (io/writer (str (get (System/getenv) "PWD")"/resources/output.json") :append true)]
    (.write wrtr (str "{""\n"
                      "   \"approved\": " "\"" card-status "\"," "\n"
                      "   \"newLimit\": " "\"" (current-acumulator limit amount count-erro) "\"," "\n"
                      "   \"deniedReasons\": " (mapv :error-body @erro) "\n"
                      "{""\n\n")))
  (reset! erro ()))

(defn rules
  "rules defined in transaction-bank/doc/intro.md"
  [card-status limit denylist merchant amount timestamp]
  (if (false? (read-string card-status))
    (status-code 20))

  (if (false? (> (bigdec limit) 0))
    (status-code 30))

  (if (.contains denylist merchant)
    (status-code 40))

  (if (or (>= (bigdec amount) (bigdec limit))
      (and (> acumulator 0) (> amount acumulator)))
    (status-code 50))

  (if (and (empty? @all-transactions)
           (false? (<= (bigdec amount) (* 0.9 (bigdec limit)))))
    (status-code 60))

  (if (.contains (comparation) merchant)
    (status-code 70))

  (if (> (count @all-transactions) 2)
    (if (<= (- (now) (first (mapv :millisecond @all-transactions))) 120000)
      (status-code 80)
      (reset! all-transactions [])))

  (output limit merchant (count @erro) amount timestamp))
