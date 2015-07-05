(ns samutamm.routes.home
  (:require [compojure.core :refer :all]
      [liberator.core :refer [defresource resource request-method-in]]
      [noir.io :as io]
      [clojure.java.io :refer [file]]
      [cheshire.core :refer [generate-string]]
      [environ.core :refer [env]]))

(defn base-dir []
  "Uggly hard coded hack that trusts that path does not change."
  (subs (System/getProperty "user.dir") 0 35))

(def index-file (let [path  (str (or (System/getenv "TRAVIS_BUILD_DIR")
                                     (base-dir))
                                 "/resources/public/index.html")]
                  (do (println path) path)))

(defresource home
    :available-media-types ["text/html"]
    :exists?
    (fn [context]
      [{::file (file index-file)}])
    :handle-ok
    (fn [{{{resource :resource} :route-params} :request}]
      (clojure.java.io/input-stream index-file)))

(defroutes home-routes
  (ANY "/" request home))
