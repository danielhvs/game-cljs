(ns game-cljs.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub :snake (fn [db _] (:snake db))) 
(rf/reg-sub :maca (fn [db _] (:maca db))) 
(rf/reg-sub :feedback (fn [db _] (:feedback db))) 
