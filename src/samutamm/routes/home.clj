(ns samutamm.routes.home
  (:require [compojure.core :refer :all]
      [liberator.core :refer [defresource resource request-method-in]]
      [noir.io :as io]
      [clojure.java.io :refer [file]]
      [cheshire.core :refer [generate-string]]))

(def index-file (str "../../../resources/public/index.html"))

(defresource home
    :available-media-types ["text/html"]
    :exists?
    (fn [context]
      [(io/get-resource index-file)
        {::file (file (str (io/resource-path) index-file))}])
    :handle-ok
    (fn [{{{resource :resource} :route-params} :request}]
      (clojure.java.io/input-stream (io/get-resource index-file))))

(defroutes home-routes
  (ANY "/" request home))
