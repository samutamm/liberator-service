(ns samutamm.routes.tags
  (:require [compojure.core :refer :all]
      [liberator.core :refer [defresource resource request-method-in]]
      [noir.io :as io]
      [clojure.java.io :refer [file]]
      [cheshire.core :refer [generate-string]]
      [samutamm.models.db :as database]
      [clojure.data.json :as json]
      [liberator.representation :refer [ring-response]]))

(defn parse-tag [context] (json/read-str
                               (slurp (get-in context [:request :body]))
                               :key-fn keyword))

(defresource get-all-tags
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok (fn[_] (generate-string (database/get-all-tags))))

(defresource add-new-tag
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :post! (fn [ctx]  { ::data (generate-string (database/add-tag (:tag (parse-tag ctx)))) })
  :handle-created ::data)

(defroutes tag-routes
  (GET "/tags" request get-all-tags)
  (POST "/tags" request add-new-tag))
