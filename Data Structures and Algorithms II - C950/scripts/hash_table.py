class MyHashTable:
    """
    A class for implementing a hash table
    """
    # Constructor with optional initial capacity.
    def __init__(self, initial_capacity=10):
        """
        Initializes a MyHastTable object
        :param initial_capacity: the initial capacity of the hash table
        """
        self.hash_table = []
        for i in range(initial_capacity):
            self.hash_table.append([])
        self.table_len = initial_capacity

    def __str__(self):
        """String method to print the hashtable items"""
        table_to_string = ''
        for i in range(self.table_len):
            for j in self.hash_table[i]:
                table_to_string += str(j) + '\n'
        return table_to_string

    def __iter__(self):
        """Iter method"""
        self.num = 0
        return self

    def __next__(self):
        """Next method"""
        if self.num >= self.table_len:
            raise StopIteration
        self.num += 1
        return self.num

    def __getitem__(self, item):
        return self.hash_table[item]

    def insert(self, item: object):
        """
        Inserts a new item into the hash table
        :param item: a new item
        :return: none
        """
        # Finds the appropriate bucket for the item within the hash table and appends the item
        self.hash_table[hash(item) % self.table_len].append(item)

    def search(self, key: object) -> object | None:
        """
        Searches for an item in the hash table
        :param key: key for the item
        :return: the item if it is in the hash table, else None
        """
        # Finds the appropriate bucket for the item within the hash table
        bucket_list = self.hash_table[hash(key) % self.table_len]
        # Searches bucket and returns the item index if the key is in that bucket, else returns None
        if key in bucket_list:
            return bucket_list[bucket_list.index(key)]
        else:
            return None

    def remove(self, key: object):
        """
        Removes an item from the hash table
        :param key: key for the item
        :return: none
        """
        bucket_list = self.hash_table[hash(key) % self.table_len]
        if key in bucket_list:
            bucket_list.remove(key)


