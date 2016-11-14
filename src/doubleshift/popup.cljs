(ns doubleshift.popup
  (:require [khroma.runtime :as runtime]
            [khroma.log :as console]
            [khroma.tabs :as tabs]
            [cljs.core.async :refer [>! <!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn set-active [tab]
  (let [{id :id} tab]
    (tabs/update id {:highlighted true})))

(defn log-tabs []
  (let [tabs-chan (tabs/query {:currentWindow true})]
    (go (when-let [alltabs (<! tabs-chan)]
          (->> alltabs
               (map #(select-keys % [:url :title :id]))
               (rand-nth)
               (set-active))))))

(defn init []
  (log-tabs)
  (console/log "hello world from doubleshift"))
