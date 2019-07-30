(ns transaction-bank.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [transaction-bank.api :as api])
  (:gen-class))

(def error-route
  {:body (str "Not Found!")})

(defn msg-index
  []
  (str "*** Access the README to execute tests ***"))

(defroutes server-routes
  (GET "/" [] (msg-index))
  (POST "/transactions" {body :body} (api/request-transaction body))
  (route/not-found (:body error-route)))

(defn attach-middleware
  "Add Ring middleware to parse json request bodies and format them later"
  [app-routes]
  (-> app-routes
    (middleware/wrap-json-body)
    (middleware/wrap-json-response)))

(def application
  (attach-middleware server-routes))

(defn -main
  [& args]
  (println "*** Web server running on port 3003 ***")
  (server/run-server application {:port 3003}))
