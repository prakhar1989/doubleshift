(ns doubleshift.popup
  (:require [khroma.runtime :as runtime]
            [khroma.log :as console]
            [khroma.tabs :as tabs]
            [cljs.core.async :refer [>! <!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn log-tabs []
  (let [tabs-chan (tabs/query {:currentWindow true})]
    (go (when-let [alltabs (<! tabs-chan)]
          (->> alltabs
               (map #(select-keys % [:url :title :id]))
               (console/log))))))

(defn init []
  (log-tabs)
  (console/log "hello world from doubleshift"))
