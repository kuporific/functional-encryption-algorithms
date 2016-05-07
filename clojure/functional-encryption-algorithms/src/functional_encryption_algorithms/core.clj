(ns functional-encryption-algorithms.core)

(defn caesar-cipher
  "Applies the caesar cipher to the given `text`, shifting by the given `shiftAmount`. Only the characters [a-z] and [A-Z] will be shifted, all other characters will remain the same."
  [text shiftAmount]
  (letfn [(shift 
           [lowest highest value] 
           (if (<= (int lowest) value (int highest)) 
               (+ shiftAmount value)
               value))]
    (->> (map int text) 
         (map (partial shift \a \z))
         (map (partial shift \A \Z)) 
         (map char) 
         (apply str))))


;; This implementation does not use the thread macro, and I find it less 
;; readable because of that.

(defn caesar-cipher2
  "Applies the caesar cipher to the given `text`, shifting by the given `shiftAmount`. Only the characters [a-z] and [A-Z] will be shifted, all other characters will remain the same."
  [text shiftAmount]
  (letfn [(shift 
           [lowest highest value] 
           (if (<= (int lowest) value (int highest)) 
               (+ shiftAmount value)
               value))]
    (apply str 
      (map char 
        (map (partial shift \A \Z) 
          (map (partial shift \a \z) 
            (map int text)))))))
