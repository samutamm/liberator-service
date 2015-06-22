(ns samutamm.routes.home
  (:require [compojure.core :refer :all]
      [liberator.core :refer [defresource resource request-method-in]]
      [noir.io :as io]
      [clojure.java.io :refer [file]]
      [cheshire.core :refer [generate-string]]))

(def users (atom ["John" "Jane"]))
(def index-file (str "/index.html"))

(defresource home
    :available-media-types ["text/html"]
    :exists?
    (fn [context]
      [(io/get-resource index-file)
        {::file (file (str (io/resource-path) index-file))}])
    :handle-ok
    (fn [{{{resource :resource} :route-params} :request}]
      (clojure.java.io/input-stream (io/get-resource index-file)))
    :last-modified
    (fn [{{{resource :resource} :route-params} :request}]
      (.lastModified (file (str (io/resource-path) index-file)))))

(defresource get-users
  :allowed-methods [:get]
  :handle-ok (fn [_] (generate-string @users))
  :available-media-types ["application/json"])

(defn check-for-empty
  [context]
  (let [params (get-in context [:request :form-params])]
    (empty? (get params "user"))))

(defresource add-user
  :method-allowed? (request-method-in :post)
  :malformed? (fn [context] (check-for-empty context))
  :handle-malformed "user name cannot be empty!"
  :post!
  (fn [context]
    (let [params (get-in context [:request :form-params])]
      (swap! users conj (get params "user"))))
  :handle-created (fn [_] (generate-string @users))
  :available-media-types ["application/json"])

(defroutes home-routes
  (ANY "/" request home)
  (GET "/users" request get-users)
  (POST "/add-user" request add-user))
