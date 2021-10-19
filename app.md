# TODOアプリ　仕様

## 要件

- 詳細はconfluence参照 </br>
  URLは諸般の事情により割愛

## DB定義

- todo

|  key              |  type            |  Constraint     |
| ---------------   | ---------------  | --------------- |
|  id ★             |  BIGINT(20)      |  unsigned NOT NULL AUTO_INCREMENT |
|  category_id      |  BIGINT(20)      |  unsigned NOT NULL |  
|  title            |  VARCHAR(255)    |  NOT NULL        |  
|  body             |  TEXT            |                  |  
|  state            |  TINYINT         |  NOT NULL        |  
|  updated_at       |  TIMESTAMP       |  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |  
|  created_at       |  TIMESTAMP       |  NOT NULL DEFAULT CURRENT_TIMESTAMP |

- to_do_category

|  key              |  type            |  Constraint     |
| ---------------   | ---------------  | --------------- |
|  id ★             |  BIGINT(20)      |  unsigned NOT NULL AUTO_INCREMENT |
|  name             |  VARCHAR(255)    |  NOT NULL |  
|  slug             |  VARCHAR(64)     |  CHARSET ascii NOT NULL |  
|  color            |  TINYINT         |  unsigned NOT NULL |   
|  updated_at       |  TIMESTAMP       |  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |  
|  created_at       |  TIMESTAMP       |  NOT NULL DEFAULT CURRENT_TIMESTAMP |

## 状態定義

- ステータス

|  ステータス         |  value           |
| ---------------   | ---------------  |
|  TODO(着手前)      |  0               |
|  進行中            |  100              |
|  完了              |  255             |
