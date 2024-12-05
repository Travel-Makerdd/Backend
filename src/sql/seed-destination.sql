-- 여행지 설명
UPDATE destination
SET destination_description = '서울은 대한민국의 수도로, 경복궁과 창덕궁 같은 전통 명소와 현대적인 동대문 디자인 플라자까지 다양한 매력을 자랑합니다. 한강 공원과 남산 타워는 도시를 더욱 특별하게 만들어줍니다.'
WHERE destination_name = '서울';

UPDATE destination
SET destination_description = '부산은 대한민국의 대표 해양 도시로, 해운대와 광안리 해변이 유명합니다. 자갈치 시장과 태종대 같은 명소에서 지역 특색과 자연의 아름다움을 즐길 수 있습니다.'
WHERE destination_name = '부산';

UPDATE destination
SET destination_description = '도쿄는 일본의 수도로, 아사쿠사와 도쿄 타워 같은 전통과 현대가 공존하는 매력적인 도시입니다. 도쿄 디즈니랜드와 스카이트리는 가족 여행객에게도 인기가 많습니다.'
WHERE destination_name = '도쿄';

UPDATE destination
SET destination_description = '오사카는 일본의 음식 문화 중심지로, 도톤보리와 오사카 성 같은 명소가 유명합니다. 타코야키와 오코노미야키 같은 현지 음식을 꼭 맛봐야 합니다.'
WHERE destination_name = '오사카';

UPDATE destination
SET destination_description = '상하이는 중국의 경제 중심지로, 와이탄과 동방명주 타워가 유명합니다. 유럽풍 건축물과 현대적인 고층 빌딩이 어우러진 독특한 도시입니다.'
WHERE destination_name = '상해';

UPDATE destination
SET destination_description = '타이베이는 대만의 수도로, 타이베이 101과 스린 야시장이 유명합니다. 전통 사원과 자연 속 양명산 국립공원은 대만의 문화와 아름다움을 보여줍니다.'
WHERE destination_name = '타이베이';

UPDATE destination
SET destination_description = '뉴욕은 세계적인 대도시로, 자유의 여신상과 타임스퀘어 같은 랜드마크가 유명합니다. 다양한 문화와 음식을 경험할 수 있는 활기찬 도시입니다.'
WHERE destination_name = '뉴욕';

UPDATE destination
SET destination_description = '캘리포니아는 금문교와 요세미티 국립공원 등 다양한 명소로 유명합니다. 로스앤젤레스와 샌디에이고에서는 자연과 도시의 매력을 모두 즐길 수 있습니다.'
WHERE destination_name = '캘리포니아';

UPDATE destination
SET destination_description = '시드니는 호주의 대표 도시로, 오페라 하우스와 본다이 비치가 유명합니다. 타롱가 동물원과 시드니 타워에서 자연과 도시의 매력을 느낄 수 있습니다.'
WHERE destination_name = '시드니';

UPDATE destination
SET destination_description = '싱가포르는 마리나 베이 샌즈와 가든스 바이 더 베이로 유명한 도시국가입니다. 차이나타운과 센토사 섬에서는 다양한 문화와 즐길 거리를 경험할 수 있습니다.'
WHERE destination_name = '싱가포르';

UPDATE destination
SET destination_description = '방콕은 태국의 수도로, 왓 포와 카오산 로드 같은 명소가 유명합니다. 시장에서 다양한 음식을 맛보고, 차오프라야 강에서 도시를 탐험할 수 있습니다.'
WHERE destination_name = '방콕';

UPDATE destination
SET destination_description = '홍콩은 동서양이 조화를 이루는 도시로, 빅토리아 피크와 스타 페리 크루즈가 유명합니다. 쇼핑과 딤섬 같은 전통 음식도 홍콩의 매력입니다.'
WHERE destination_name = '홍콩';

-- 여행지 이미지 URL
INSERT INTO destination_image (destination_id, destination_image_url) VALUES
-- 대한민국
(1, '/images/seoul.jpg'),
(2, '/images/busan.jpg'),

-- 일본
(3, '/images/tokyo.jpg'),
(4, '/images/osaka.jpg'),

-- 중국
(5, '/images/shanghai.jpg'),

-- 대만
(6, '/images/taipei.jpg'),

-- 홍콩
(7, '/images/hongkong.jpg'),

-- 미국
(8, '/images/newyork.jpg'),
(9, '/images/california.jpg'),

-- 호주
(10, '/images/sydney.jpg'),

-- 싱가포르
(11, '/images/singapore.jpg'),

-- 태국
(12, '/images/bangkok.jpg');

