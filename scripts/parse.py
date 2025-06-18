import os
import csv
import json

field_names = [
    "benchmark", "benchtype", "benchconf", "time_stamp", "samples",
    "nodes", "edges", "heaps", "stacks", "globals",
    "instructions", "basic_blocks", "metadata", "transfers",
    "functions", "libraries",
    "static_calls", "direct_calls", "indirect_calls", "parameter_calls", "structure_calls",
    "parse_time", "graph_time", "type_time", "pointer_time", "query_time",
    "true_positive", "true_negative", "false_positive", "false_negative",
    "type_target", "type_number",
    "escape_target", "escape_number",
    "pointer_target", "pointer_number",
    "combined_target", "combined_number",
    "failure_number"
    ]

def parse(json_file:str, writer:csv.DictWriter):
    if not json_file.endswith('.json'):
        return
    with open(json_file, 'rt') as fs:
        raw_data = json.load(fs)
    writer.writerow(raw_data)

def parseSection(input_path:str, filename:str):
    global result_path, field_names
    csv_file = os.path.join(result_path, f's{filename}.csv')
    with open(csv_file, 'wt') as fs:
        writer = csv.DictWriter(fs, field_names)
        writer.writeheader()
        list(map(lambda x: parse(os.path.join(input_path, x), writer), os.listdir(input_path)))

def parseJson(input_file:str, filename:str):
    global result_path, field_names
    name, ext = os.path.splitext(filename)
    if ext != '.json':
        return
    csv_file = os.path.join(result_path, f'j{name}.csv')
    with open(csv_file, 'wt') as fs:
        writer = csv.DictWriter(fs, field_names)
        writer.writeheader()
        parse(input_file, writer)

def runSection(filename:str):
    global result_path
    file_path = os.path.join(result_path, filename)
    if os.path.isfile(file_path):
        parseJson(file_path, filename)
    else:
        parseSection(file_path, filename)

def buildResult(base_path):
    global result_path
    result_path = os.path.join(base_path, 'results')
    if os.path.exists(result_path):
        list(map(runSection, os.listdir(result_path)))