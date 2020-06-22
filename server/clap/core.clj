(ns clap.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [resources]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [response resource-response]]))

(defn ping-handler [req]
  (response {:message "🏓 pong!"}))

(defroutes
  routes
  (GET "/api/ping" [] ping-handler)
  (resources "/" {:root "build"})
  (GET "*" [] (fn [_] (response {:message "404"}))))

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))
        app (-> routes
                wrap-keyword-params
                ring.middleware.params/wrap-params
                (wrap-json-body {:keywords? true})
                wrap-json-response)]
    (println (str "🚀 app on " port))
    (jetty/run-jetty app {:port port})))