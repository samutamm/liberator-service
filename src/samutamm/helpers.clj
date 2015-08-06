(ns samutamm.helpers)

(defn make-sql-date [year month day]
  (java.sql.Date.
   (.getTimeInMillis
    (java.util.GregorianCalendar. year month day))))

(def exampleproject {:id 1 :projectname "Proju" :description "cool" :tags "#"
                           :links "http://osote.fi"
                           :projectstart {:year 2000 :month 6 :day 10} :projectend {:year 2000 :month 8 :day 10}})

(defn format-date [date-hash]
  (make-sql-date (:year date-hash) (:month date-hash) (:day date-hash)))
