(ns clap.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [resources]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :as resp :refer [response resource-response]]))

(defn ping-handler [req]
  (response {:message "🏓 pong!"}))

(def static-root "public")
(defn render-static-file [filename]
  (resp/content-type
    (resp/resource-response filename {:root static-root}) "text/html"))

(defroutes
  routes

  ;; ---
  ;; api

  (GET "/api/ping" [] ping-handler)

  ;; ---
  ;; serve static assets

  (resources "/" {:root static-root})
  (GET "*" [] (render-static-file "index.html")))

(defn wrap-cors [handler]
  (fn [request]
    (assoc-in (handler request)
              [:headers "Access-Control-Allow-Origin"]
              "*")))
;; ---
;; serve static assets

(defn -main [& _args]
  (let [port (Integer/parseInt (System/getenv "PORT"))
        app (-> routes
                wrap-cors
                wrap-keyword-params
                ring.middleware.params/wrap-params
                (wrap-json-body {:keywords? true})
                wrap-json-response)]
    (println (str "🚀 app on " port))
    (jetty/run-jetty app {:port port})))