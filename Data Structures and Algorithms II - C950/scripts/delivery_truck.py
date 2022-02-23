from typing import List
import datetime
from scripts.wgups_package import WGUPSPackage
from scripts.delivery_algorithm import DeliveryNode, nearest_neighbor, get_distance


class DeliveryTruck:
    """
    Class for a DeliveryTruck object to deliver packages
    """
    def __init__(self, truck_name: str, assigned_zips: List, distance_driven=0):
        """
        Initializes a DeliveryTruck object
        :param truck_name: name for the truck
        :param assigned_zips: zip codes the truck is assigned to deliver to
        :param distance_driven: how far the truck has driven
        """
        self.packages = []
        self.truck_name = truck_name
        self.assigned_zips = assigned_zips
        self.distance_driven = distance_driven
        self.delivery_route = []
        self.time_elapsed = 0.0

    def load_package(self, package: WGUPSPackage):
        """
        Loads a package into the truck
        :param package: package
        :return: None
        """
        if len(self.packages) < 16:
            self.packages.append(package)
        else:
            print(self.truck_name + ' is full.')

    def is_full(self):
        """
        Checks if the truck is full
        :return: bool
        """
        return len(self.packages) == 16

    def build_route(self, distance_table: List):
        """
        Builds the delivery route for the truck
        :param distance_table: Array containing delivery distances between addresses
        :return: None
        """
        hub_node = DeliveryNode(WGUPSPackage(0, '4001 South 700 East', 'Salt Lake City UT', '84107', '00:00',
                                             0, 'Hub', 'None'))
        # Add the hub as the first node
        self.delivery_route.append(hub_node)
        # Add the rest of the packages to the route
        for package in self.packages:
            self.delivery_route.append(DeliveryNode(package))

        # Set the delivery route based on the nearest neighbor
        self.delivery_route = nearest_neighbor(self.delivery_route, distance_table)

        # Add end node to get back to the hub
        end_node = DeliveryNode(WGUPSPackage(9999, '4001 South 700 East', 'Salt Lake City UT', '84107', '00:00',
                                             0, 'Hub', 'None'))
        end_node.distance_from_previous = get_distance(self.delivery_route[-1], end_node, distance_table)
        self.delivery_route.append(end_node)

    def deliver_packages(self, current_time: datetime.datetime, report_time: datetime.datetime,
                         distance_table: List) -> datetime.datetime:
        """
        Delivers packages
        :param current_time: time the truck first leaves
        :param report_time: time to report back status of packages/stop delivering
        :param distance_table:
        :return: Time truck returns to the hub
        """
        for delivery_node in self.delivery_route:
            delivery_node.package.stats = 'en route'
        for delivery_node in self.delivery_route:
            # Changes delivery address of package 9 on the fly, which does not happen until after 10:20AM
            if delivery_node.package.package_id == 9:
                delivery_node.package.delivery_address = '410 S State St'
                self.delivery_route = self.delivery_route[self.delivery_route.index(delivery_node):]
                self.delivery_route = nearest_neighbor(self.delivery_route, distance_table)
                print('Package 9 address updated at ' + str(current_time.time()))
            current_time = current_time + datetime.timedelta(hours=(delivery_node.distance_from_previous / 18))
            if current_time > report_time:
                return current_time
            delivery_node.package.status = 'delivered'
            self.distance_driven += delivery_node.distance_from_previous
            delivery_node.package.delivered_time = current_time
        return current_time
