(ns functional-encryption-algorithms.caesar-cipher-test
  (:require [clojure.test :refer :all]
            [functional-encryption-algorithms.caesar-cipher :refer :all :as caesar-cipher])
  (:import org.apache.commons.lang3.RandomStringUtils))

(deftest test-caesar-cipher-inverse
  (testing "Test that the ceasar cipher can be applied forward and backward"
    (are [original decrypted] apply (= original decrypted)
    (mapcat #(list % (caesar-cipher/encrypt (caesar-cipher/encrypt % 1) -1))
      (take 100 (repeatedly #(RandomStringUtils/random 50)))))))
