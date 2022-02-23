from typing import List


class DeliveryNode:
    """
    Class for a DeliveryNode
    """
    def __init__(self, package):
        self.package = package
        self.distance_from_previous = 0.0


def nearest_neighbor(package_list: List, distance_table: List) -> List:
    """
    Nearest neighbor algorithm finds the next best delivery location based on closeness to current node
    :param package_list: list of packages
    :param distance_table: array of addresses and distances from each other
    :return: finished route
    """
    # Set up list for the route
    route = []

    # Choose starting node, which is the hub, and pop it from the list
    current_node = package_list[0]
    route.append(package_list.pop(package_list.index(current_node)))

    # While there are still packages in the package list
    while len(package_list) > 0:
        # Set initial min distance to infinite
        min_distance = float('inf')
        # Loop all nodes that are left in the package list
        for next_node in package_list:
            # Get the distance from the current package and the next one in the iteration
            distance = get_distance(current_node, next_node, distance_table)
            # If this package is closer than the current closest, replace it as the closest node and reset the min distance
            if distance < min_distance:
                closest_node = next_node
                min_distance = distance
        # Once the loop has completed we have the closest node
        # Set the closest node's distance to the last min distance, the distance from the previous node
        closest_node.distance_from_previous = min_distance
        # Set the new current node to the closest node
        current_node = closest_node
        # Pop the node from the list
        route.append(package_list.pop(package_list.index(current_node)))
    return route


def get_distance(node1: DeliveryNode, node2: DeliveryNode, distance_table: List) -> int:
    """
    Gets distance between two addresses
    :param node1: first node
    :param node2: second node
    :param distance_table: table of addresses and distances between them
    :return: distance
    """
    # Gets addresses from nodes and their indexes in the first row of the distance table
    address_1 = node1.package.delivery_address
    address_2 = node2.package.delivery_address
    address_1_index = distance_table[0].index(address_1)
    address_2_index = distance_table[0].index(address_2)
    # Checks which index is less. The greater the index the further down the table the address is, therefore we want
    # to use its row to find the other address
    if address_1_index < address_2_index:
        destination_address = address_2
        horizontal_index = address_1_index + 1
    else:
        destination_address = address_1
        horizontal_index = address_2_index + 1

    # Go to the destination's row in the first column
    for row in distance_table:
        if row[0] == destination_address:
            # Distance is the horizontal index in plus 1
            distance = float(row[horizontal_index])

    return distance


