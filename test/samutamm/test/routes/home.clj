(ns samutamm.test.routes.home
  (:use midje.sweet
        samutamm.routes.home))

(fact "home knows where html lies"
      index-file => (fn [text] (.contains text "index.html")))

;; handler test tests rest of the logic.
